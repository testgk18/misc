#! /bin/sh

FILES=`find . -name '*.tgz'`
#FILES=`find . -type d -maxdepth 1 -mindepth 1`

for i in $FILES;
#	do rm -rf $i;
#	do tar -czf $i.tgz $i;
	do tar -xzf $i
done;
