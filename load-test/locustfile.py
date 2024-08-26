import random

from locust import task, FastHttpUser

class EventOrderProduct(FastHttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0
    @task
    def order(self):
        member_id = random.randint(1, 4000)
        json_body = {
            "memberId": member_id,
            "eventId": 1,
            "quantity": 1
        }
        jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MTIzQGdtYWlsLmNvbSIsImF1dGgiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjQ2NTc4NzUsImV4cCI6MTcyNDY2MTQ3NX0.wojXqFRwCTUk9z630oravKZ89hi1ncBa5f5k8a_abG3bszy4RStXl2EOWV5E6NX9gSU-YdAHc2pHcvJfdFsddg"
        headers = {"Authorization": "Bearer " + jwt}

        # POST 요청 보내기
        self.client.post("/api/v1/orders/asyncEventProducts/9", json=json_body, headers=headers)

