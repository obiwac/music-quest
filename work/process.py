# this script creates a pixel-perfect lightmap
# TODO to deal with overlapping UV's, instead of wasting space by making them super big, we can understand another UV map called something like "packed UV's"

# instructions:
# - each object should be entirely described by one UV map with no overlapping parts (the UV can be bigger than the unit square though)
# - you must select the texture node in the shader for each object
# - you must create an image node starting with the string "lightmap" in order for this script to place the created lightmap texture into it

# a few tips when baking:
# - do not use denoising. it makes these ugly black border artifacts and makes certain things look blurry.
# - join all of the objects before baking, it makes things a lot quicker (I think the alternative is to separately bake for each object?)

import bpy
import math
import rpack # from pip, rectangle-packer

scene = bpy.context.scene

textures = {}
relevant = []

def is_lightmap(node):
	return node.type == "TEX_IMAGE" and node.image.name.startswith("lightmap")

print("Get textures from objects (make sure you're in object more for this!)")

sizes = []
residuals = []
real_sizes = []

for obj in scene.objects:
	if obj.type != "MESH":
		continue

	if obj.name.startswith("collider-"):
		continue

	if len(obj.material_slots) == 0:
		print(f"WARNING: {obj.name} has no material slots. Ignoring.")
		continue

	mat_slot = obj.material_slots[0]
	mat = mat_slot.material
	assert mat is not None # this shouldn't happen for any material slot

	node_tree = mat.node_tree
	assert node_tree is not None and node_tree.nodes is not None # this shouldn't happen for any material

	selected_node = None

	for node in node_tree.nodes:
		if node.type == "TEX_IMAGE" and node.select and not is_lightmap(node):
			selected_node = node
			break

	if selected_node is None:
		print(f"WARNING: {obj.name} has no selected texture image node. Ignoring.")
		continue

	image = selected_node.image
	assert image is not None # this shouldn't happen for any texture image node

	width, height = image.size

	if (width > 512 or height > 512) and obj.name != "ground":
		print(f"WARNING: {obj.name} has a texture image that is larger than 512x512 ({width}x{height}). Ignoring.")
		continue

	relevant.append(obj)

	# get uv map bounds

	uv_map = obj.data.uv_layers["UVMap"].data
	min_x = min_y = float("inf")
	max_x = max_y = float("-inf")

	for face in obj.data.polygons:
		for loop_index in face.loop_indices:
			if len(uv_map) <= loop_index:
				print(f"WARNING: {obj.name} has a face with a loop index that is out of bounds. Ignoring. Are you sure you're in object mode?")
				break

			uv = uv_map[loop_index].uv

			min_x = min(min_x, uv.x)
			min_y = min(min_y, uv.y)
			max_x = max(max_x, uv.x)
			max_y = max(max_y, uv.y)

	fixed_min_x = math.floor(min_x * width)
	fixed_min_y = math.floor(min_y * height)

	fixed_max_x = math.ceil(max_x * width)
	fixed_max_y = math.ceil(max_y * height)

	total_width = fixed_max_x - fixed_min_x
	total_height = fixed_max_y - fixed_min_y

	sizes.append((total_width + 2, total_height + 2)) # +2 for margin

	# residual and real size calculation
	# these are to ensure our UV maps start and end exactly where they should within a pixel

	residual_min_x = min_x * width - fixed_min_x
	residual_min_y = min_y * height - fixed_min_y

	real_width = (max_x - min_x) * width
	real_height = (max_y - min_y) * height

	residuals.append((residual_min_x, residual_min_y))
	real_sizes.append((real_width, real_height))

print("Rectangle packing starts")
positions = rpack.pack(sizes, 4096, 4096)
print("Rectangle packing ends")

print("Get full lightmap texture size")

lightmap_width = 0
lightmap_height = 0

for position, size in zip(positions, sizes):
	x, y = position
	w, h = size

	lightmap_width = max(lightmap_width, x + w)
	lightmap_height = max(lightmap_height, y + h)

print(f"Lightmap size: {lightmap_width}x{lightmap_height}")

if lightmap_width > 4096 or lightmap_height > 4096:
	print("ERROR: Lightmap size is too large. Maximum is 4096x4096.")
	exit()

print("Create lightmap UV map for each object")

for i, obj in enumerate(relevant):
	size = sizes[i]
	pos = positions[i]
	residual = residuals[i]
	real_size = real_sizes[i]

	# remove the margin

	position = (pos[0] + 1, pos[1] + 1)
	size = (size[0] - 2, size[1] - 2)

	# create UV map named "lightmap"

	if obj.data.uv_layers.get("lightmap") is None:
		obj.data.uv_layers.new(name="lightmap")

	lightmap_uv_map = obj.data.uv_layers["lightmap"].data
	uv_map = obj.data.uv_layers["UVMap"].data

	# get existing bounds of original UV map

	min_u = min_v = float("inf")
	max_u = max_v = float("-inf")

	for face in obj.data.polygons:
		for loop_index in face.loop_indices:
			uv = uv_map[loop_index].uv

			min_u = min(min_u, uv.x)
			min_v = min(min_v, uv.y)
			max_u = max(max_u, uv.x)
			max_v = max(max_v, uv.y)

	# map current UV map to position and size within the lightmap

	for face in obj.data.polygons:
		for loop_index in face.loop_indices:
			uv = uv_map[loop_index].uv
			lightmap_uv = lightmap_uv_map[loop_index].uv

			u = (uv.x - min_u) / (max_u - min_u)
			v = (uv.y - min_v) / (max_v - min_v)

			lightmap_uv.x = (position[0] + u * real_size[0] + residual[0]) / lightmap_width
			lightmap_uv.y = (position[1] + v * real_size[1] + residual[1]) / lightmap_height

# create a new image for the lightmap

lightmap = bpy.data.images.new("lightmap", width=lightmap_width, height=lightmap_height, alpha=True)
print("Created lightmap:", lightmap.name)

# make lightmap node of each shader the new one, and select it

print("Set lightmap image for each object")

for obj in relevant:
	mat_slot = obj.material_slots[0]
	mat = mat_slot.material
	node_tree = mat.node_tree

	# select lightmap node
	# TODO create node if it doesn't yet exist?

	for node in node_tree.nodes:
		if node.type == "TEX_IMAGE" and is_lightmap(node):
			node.image = lightmap
			node.select = True

			node_tree.nodes.active = node
			break

	# select lightmap UV map

	obj.data.uv_layers.active_index = obj.data.uv_layers.find("lightmap")

print("Ready to bake!")

"""
print("Get mega texture size")

PADDING = 1
MARGIN = 1

tex_x = {}
tex_y = PADDING # the same for all textures :)

mega_tex_width = 0
mega_tex_height = 0

for k, v in textures.items():
	image = v["image"]
	width, height = image.size

	tex_x[k] = PADDING + mega_tex_width
	mega_tex_width += width + MARGIN

	mega_tex_height = max(mega_tex_height, height)

mega_tex_width += PADDING * 2
mega_tex_height += PADDING * 2

print(f"Mega texture size: {mega_tex_width}x{mega_tex_height}")

print("Create mega texture")

mega_tex = bpy.data.images.new("mega_texture", width=mega_tex_width, height=mega_tex_height, alpha=True)
mega_tex_pitch = mega_tex_width * 4
mega_tex_pixels = [0] * mega_tex_pitch * mega_tex_height

print("Copy textures to mega texture")

for k, v in textures.items():
	image = v["image"]
	width, height = image.size
	pitch = width * 4

	begin_x = tex_x[k]
	begin_y = tex_y

	pixels = image.pixels[:] # copy cuz direct indexing is slow

	print(image.name)

	for y in range(height):
		for x in range(width):
			for i in range(4):
				mega_tex_pixels[(begin_y + y) * mega_tex_pitch + 4 * (begin_x + x) + i] = pixels[y * pitch + x * 4 + i] 

mega_tex.pixels = mega_tex_pixels
"""
