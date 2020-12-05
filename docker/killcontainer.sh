#!/bin/sh
DOCKER_CMD="sudo docker"
PS_CMD="${DOCKER_CMD} ps -a"
kill_one () {
param=$1
for cid in `sudo docker ps |grep $param | awk '{print $1}'`
do
echo $cid
${DOCKER_CMD} container stop $cid
${DOCKER_CMD} container rm $cid
done

${PS_CMD}
}


kill_all () {
for cid in `${DOCKER_CMD} ps -aq`
do
echo $cid
${DOCKER_CMD} container stop $cid
${DOCKER_CMD} container rm $cid

done

${PS_CMD}
}

#kill_one "ganesan-postgres"
kill_all ()
