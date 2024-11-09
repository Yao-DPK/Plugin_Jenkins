docker run -d \
  --name powerapi-mongodb \
  --env-file .env_mongodb \
  -p 27017:27017 \
  -v ${DOCKER_POWERAPI_STORAGE}mongodb-storage:/data/db \
  mongo

docker run -d \
  --name powerapi-influxdb \
  --env-file .env_influxdb \
  -p 8086:8086 \
  -v ${DOCKER_POWERAPI_STORAGE}influxdb-storage:/var/lib/influxdb \
  --network host \
  influxdb:1.8-alpine

docker run -d \
  --name powerapi-grafana \
  --env-file .env_grafana \
  -p 3000:3000 \
  -v ${DOCKER_POWERAPI_STORAGE}grafana-storage:/var/lib/grafana \
  --network host \
  --user "$UID:$GID" \
  grafana/grafana

docker run -d \
  --name powerapi-smarttwatts-formula \
  -v ./config_file_sw.json:/config_file_sw.json \
  --network host \
  powerapi/smartwatts-formula:0.9.2 \
  powerapi/smartwatts-formula --config-file /config_file_sw.json


docker run -d \
  --name powerapi-sensor \
  -v /sys:/sys \
  -v /var/lib/docker/containers:/var/lib/docker/containers:ro \
  -v ./config_file_hwpc.json:/config_file_hwpc.json \
  --network host \
  --privileged \
  powerapi/hwpc-sensor:1.1.2 \
  -n powerapi/hwpc-sensor --config-file /config_file_hwpc.json

