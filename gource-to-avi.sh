#!/bin/bash

ffmpeg -y -r 60 -f image2pipe \
    -vcodec ppm -i gource.ppm \
    -vcodec libx264 -preset medium \
    -pix_fmt yuv420p \
    -crf 10 -threads 0 \
    -bf 0 gource.x264.avi && rm gource.ppm