#!/bin/bash


PRJDIR="$(pwd)";

###################################
#(函数)判断程序是否已启动
# 读取PRJDIR下的server.pid文件
# 根据读取的值判断ps -c 是否存在
###################################
psid=0;

checkpid() {
	if [ -e ${PRJDIR}/server.pid ]; then
		content=`cat ${PRJDIR}/server.pid | awk '{print $1}'`
		psid=$content
	fi
	if [ $psid -ne 0 ]; then
		javaps=`jps -l | grep $psid`
		echo $javaps
		if [ -n "$javaps" ]; then
			psid=`echo $javaps | awk '{print $1}'`
		else 
			psid=0
		fi
	else
		echo "psid=$psid application is not exist"
	fi
}


stop(){
	checkpid;
	if [ $psid -eq 0 ]; then
		echo "application not startup..........."
	else
		echo `kill -9 $psid`
	fi
}

stop

