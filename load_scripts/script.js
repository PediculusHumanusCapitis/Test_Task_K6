import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  stages: [
    { duration: '1s', target: 5},
    { duration: '5m', target: 5},
    { duration: '1s', target: 10},
    { duration: '5m', target: 10},
  ],
};

export default function () {
  const payload = JSON.stringify({ userId: Math.floor(Math.random() * 10000) });
  const params = { headers: { 'Content-Type': 'application/json' } };
  const res = http.post('http://localhost:8081/kafka/send', payload, params);
  check(res, { 'status is 200': (r) => r.status === 200 });
  sleep(1);
}