#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>
int sum = 0;  // a shared variable

sem_t *sem;

void *counter(void *param)
{
    int k;
    for (k = 0; k < 10000; k++)
    {
        /* entry section */
        sem_wait(sem);
        /* critical section */
        sum++;
        /* exit section */
        sem_post(sem);
        /* remainder section */
    }
    pthread_exit(0);
}

int main()
{
    pthread_t tid1, tid2;
    
    //sem_init(&sem, 0, 1);
    sem = sem_open("test_sem", O_CREAT | O_EXCL, 0644, 1); 
    pthread_create(&tid1, NULL, counter, NULL);
    pthread_create(&tid2, NULL, counter, NULL);
    pthread_join(tid1, NULL);
    pthread_join(tid2, NULL);
    int ret = sem_close(sem); // 세마포어 종료 및 할당한 자원 해제
	//printf("sem_close: %d\n", ret);
	ret = sem_unlink("test_sem"); // 세마포어 객체 해제
	//printf("sem_unlink: %d\n", ret);
    printf("sum = %d\n", sum);
}