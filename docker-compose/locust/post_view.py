import random
from locust import HttpUser, task, between

class PostApiUser(HttpUser):
    wait_time = between(1, 3)  # 각 요청 사이의 대기 시간 (1~3초 랜덤)

    @task(3)  # 실행 확률 (높을수록 자주 실행)
    def get_post(self):
        post_id = random.randint(4585, 4873)
        self.client.get(f"/posts/{post_id}", name="/posts/{postId}")

    @task(2)
    def get_post_comments(self):
        post_id = random.randint(4585, 4873)
        self.client.get(f"/posts/posts/{post_id}/comments", name="/posts/posts/{postId}/comments")

    @task(1)
    def get_post_likes(self):
        post_id = random.randint(4585, 4873)
        self.client.get(f"/posts/posts/{post_id}/like", name="/posts/posts/{postId}/like")
