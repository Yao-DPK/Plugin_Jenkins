#!/usr/bin/env bash

[ ! -f .env_mongodb ] || export $(grep -v '^#' .env_mongodb | xargs)
[ ! -f .env_influxdb ] || export $(grep -v '^#' .env_influxdb | xargs)

maxfrequency=$(lscpu -b -p=MAXMHZ | tail -n -1| cut -d , -f 1)
minfrequency=$(lscpu -b -p=MINMHZ | tail -n -1 | cut -d , -f 1)
basefrequency=$(lscpu | grep "Model name" | cut -d @ -f 2 | cut -d G -f 1)
basefrequency=$(expr ${basefrequency}\*1000 | bc | cut -d . -f 1)

echo "
{
  \"verbose\": false,
  \"stream\": true,
  \"input\": {
    \"puller\": {
      \"model\": \"HWPCReport\",
      \"type\": \"mongodb\",
      \"uri\": \"mongodb://${MONGO_INITDB_ROOT_USERNAME}:${MONGO_INITDB_ROOT_PASSWORD}@${MONGODB_HOST}:${MONGODB_PORT}\",
      \"db\": \"${MONGODB_NAME}\",
      \"collection\": \"hwpc_report\"
    }
  },
  \"output\": {
    \"pusher_power\": {
      \"type\": \"influxdb\",
      \"tags\": \"socket\",
      \"uri\": \"${INFLUXDB_HOST}\",
      \"port\": ${INFLUXDB_PORT},
      \"db\": \"${INFLUXDB_NAME}\"
    }
  },
  \"cpu-frequency-base\": $basefrequency,
  \"cpu-frequency-min\": $minfrequency,
  \"cpu-frequency-max\": $maxfrequency,
  \"cpu-error-threshold\": 2.0,
  \"disable-dram-formula\": true,
  \"sensor-report-sampling-interval\": 1000
}
" > ./config_file_sw.json

echo "
{
  \"name\": \"powerapi/hwpc-sensor\",
  \"verbose\": false,
  \"frequency\": 1000,
  \"output\": {
    \"type\": \"mongodb\",
    \"uri\": \"mongodb://${MONGO_INITDB_ROOT_USERNAME}:${MONGO_INITDB_ROOT_PASSWORD}@${MONGODB_HOST}:${MONGODB_PORT}\",
    \"database\": \"${MONGODB_NAME}\",
    \"collection\": \"hwpc_report\"
  },
  \"system\": {
    \"rapl\": {
      \"events\": [\"RAPL_ENERGY_PKG\"],
      \"monitoring_type\": \"MONITOR_ALL_CPU_PER_SOCKET\"
    },
    \"msr\": {
      \"events\": [\"TSC\", \"APERF\", \"MPERF\"]
    }
  },
  \"container\": {
    \"core\": {
      \"events\": [
        \"CPU_CLK_THREAD_UNHALTED:REF_P\",
        \"CPU_CLK_THREAD_UNHALTED:THREAD_P\",
        \"LLC_MISSES\",
        \"INSTRUCTIONS_RETIRED\"
      ]
    }
  }
}
" > ./config_file_hwpc.json

