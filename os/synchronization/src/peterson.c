#include <stdio.h>
#include <pthread.h>

#define true 1
#define false 0

int sum = 0;

int turn;
int flag[2];

void *producer(void *param)
{
	int k;
	for (k = 0 ; k < 10000; k++)
	{
		flag[0] = true;
		turn = 1;
		while (flag[1] && turn == 1)
			;
		sum++;

		flag[0] = false;
	}
	pthread_exit(0);
}

void *consumer(void *param)
{
	int k;
	for (k = 0 ; k < 10000; k++)
	{
		flag[1] = true;
		turn = 0;
		while (flag[0] && turn == 0)
			;
		sum--;

		flag[1] = false;
	}
	pthread_exit(0);
}
int main(void)
{
	pthread_t tid1, tid2;
	pthread_create(&tid1, NULL, producer, NULL);
	pthread_create(&tid2, NULL, consumer, NULL);
	pthread_join(tid1, NULL);
	pthread_join(tid2, NULL);
	printf("sum = %d\n", sum);
}