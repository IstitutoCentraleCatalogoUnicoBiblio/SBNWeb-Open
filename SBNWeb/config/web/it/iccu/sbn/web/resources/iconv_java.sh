#!/bin/sh

for i in `ls *.properties`; do
  iconv -f UTF-8 -t JAVA $i > $i.utf8
  mv $i.utf8 $i
done