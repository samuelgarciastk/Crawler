#!/bin/sh
cd /home/stk/Documents/result
touch list
for i in {1..$1}
do
    cat "page$i" >> list
done
