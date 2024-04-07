#!/bin/sh
set -xe

~/random/obj-to-ivx/obj-to-ivx models/map.obj ../app/src/main/assets/map.ivx
CompressonatorCLI -log -fd ETC2_RGBA1 textures/map.png ../app/src/main/assets/textures/map.ktx