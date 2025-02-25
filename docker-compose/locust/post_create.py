from locust import HttpUser, task, between
import random
import string

class PostUser(HttpUser):

    @task
    def create_post(self):
        url = "/posts"
        payload = {
            "userId": random.randint(1, 100000),  # 임의의 사용자 ID
            "title": f"title - {random.randint(1, 1000000)}",
            "content": f"content - {random.randint(1, 100000)}",
            "categoryId": random.randint(1, 30)  # 1~10 사이의 랜덤 카테고리 ID
        }
        headers = {"Content-Type": "application/json"}
        self.client.post(url, json=payload, headers=headers)