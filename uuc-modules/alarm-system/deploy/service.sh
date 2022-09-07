#!/bin/bash

source /etc/profile
ServiceName=alarm-system

#### 启动脚本
start() {
  CMD_PATH="/data/service/${ServiceName}"
  cd $CMD_PATH/ || exit
  java -jar -Xms512m -Xmx1024m -Xmn64m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC ${ServiceName}.jar --server.port=9515 --spring.profiles.active=dev --spring.cloud.bootstrap.location=./config/ >>./logs/console.log 2>&1 &
  echo $! >$CMD_PATH/${ServiceName}.pid
}

#### 停止脚本
stop() {
  CMD_PATH="/data/service/${ServiceName}"
  PID=$(cat $CMD_PATH/${ServiceName}.pid)
  kill -9 "$PID"
}

#### 查看日志
showlog() {
  CMD_PATH="/data/service/${ServiceName}"
  cd $CMD_PATH/ || exit
  tail -fn 300 $CMD_PATH/logs/console.log
  echo $! >$CMD_PATH/${ServiceName}.pid
}

case $1 in
showlog)
  showlog
  ;;
start)
  start
  ;;
stop)
  stop
  ;;
restart)
  stop
  start
  ;;
esac
