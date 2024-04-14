import bpy

SCALE = .15

def find_extreme_points(global_vertices: list):
	most_positive = [-float('inf'), -float('inf'), -float('inf')]
	most_negative = [float('inf'), float('inf'), float('inf')]

	for v in global_vertices:
		for i in range(3):
			most_positive[i] = max(most_positive[i], v[i] * SCALE)
			most_negative[i] = min(most_negative[i], v[i] * SCALE)

	return most_positive, most_negative


def process_meshes(collection):
	s = "colliders = arrayOf(\n"

	for obj in collection.objects:
		mesh = obj.data
		global_vertices = [obj.matrix_world @ v.co for v in mesh.vertices]
		mp, mn = find_extreme_points(global_vertices)
		s += f"\tCollider({mn[0]}f, {mn[2]}f, {mn[1]}f, {mp[0]}f, {mp[2]}f, {mp[1]}f),\n"

	s += ")"
	print(s)

collection = bpy.data.collections.get("colliders")
if collection:
	process_meshes(collection)
else:
	print("Collection not found.")
