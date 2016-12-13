# sb-simple-cache-java


캐싱전략 1. 데이터 호출시 일정시간체크

init - 선언할 때 가져올지 처음쓸 때 가져올지
fifo
ttl 
loadOne : sync/async - 새 데이터를 조회할 때 기다릴지 이전걸 가져갈지
이전걸 가져가는 경우 최초 데이터라면
refresh : one/all - 한개씩 가져올지 전체 다 가져올지


https://hazelcast.org/ 이거참고 또는 이용

