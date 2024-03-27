# 将IP地址分配给本地回环接口（lo0）
# (where 10.200.10.1 is some unused IP address)
sudo ifconfig lo0 alias 10.200.10.1/24
export DOCKER_HOST_IP=10.200.10.1
export EVENTUATE_COMMON_VERSION=0.18.0.RELEASE
export EVENTUATE_MESSAGING_KAFKA_IMAGE_VERSION=0.18.0.RELEASE
export EVENTUATE_CDC_VERSION=0.16.0.RELEASE

# 启动
docker-compose -f docker-compose-mysql-binlog.yml up -d
# 停止
docker-compose -f docker-compose-mysql-binlog.yml down -v
