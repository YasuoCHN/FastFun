#!/bin/bash


PRJDIR="$(pwd)";
echo $PRJDIR;
echo "Application Root Dir:${PRJDIR}";

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
			echo "find application psid=$psid is running!"
			psid=`echo $javaps | awk '{print $1}'`
		else 
			psid=0
		fi
	else
		echo "psid=$psid application is not exist"
	fi
}

app(){
	CP=.
	for i in lib/*.jar ;
	do
  	  CP=${i}:${CP}
	done

	for i in ../../lib/*.jar;
	do
  	  CP=${i}:${CP}
	done

	CP=${CP}

	JVM_PARAMS="-server -Xms512m -Xmx1024m -Dclient.encoding.override=utf-8\
           -Dserver.encoding.override=utf-8 \
           -Dfile.encoding=utf-8 -Duser.language=zh -Duser.region=CN \
           -XX:+UseConcMarkSweepGC"

	cmd="java -cp ${CP} ${JVM_PARAMS} com.klw.fastfun.pay.view.app.ServerBootstrap ${*}"
	echo `nohup $cmd > /data/log/ffserver.log 2>&1 &`
}

start(){
	checkpid;
	if [ $psid -eq 0 ]; then
		echo "start application................."
		app
	else
		echo "application is running, you must kill -9 $psid"
	fi
}

start
