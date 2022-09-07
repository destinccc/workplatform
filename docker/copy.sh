#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/uuc/ry_init.sql ./mysql/db
cp ../sql/uuc/ry_config_init.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../uuc-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy uuc-gateway "
cp ../uuc-gateway/target/uuc-gateway.jar ./uuc/gateway/jar

echo "begin copy uuc-auth "
cp ../uuc-auth/target/uuc-auth.jar ./uuc/auth/jar

echo "begin copy uuc-modules-system "
cp ../uuc-modules/uuc-system/target/uuc-modules-system.jar ./uuc/modules/system/jar