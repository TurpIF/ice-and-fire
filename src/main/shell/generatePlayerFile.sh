#!/usr/bin/env bash

# To be run from root of this project

cat ./out/chelper/output/Player.java | sed "s/public class Player {/class Player {/" > ./out/Player.java