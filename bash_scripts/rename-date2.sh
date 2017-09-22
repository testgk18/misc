#! /bin/sh

# renames files according to their last modified date
find . -depth -name '* *' 
find . -type f -print0 | while IFS= read -r -d $'\0' file; do
  t=`date -r "${file}" "+%Y-%m-%d_%H-%M"`
  newName=${t}_screenshot.png
  echo $newName
done
