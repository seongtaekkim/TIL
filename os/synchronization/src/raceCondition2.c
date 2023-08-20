#include <stdio.h>
#include <pthread.h>

int sum;

void *run(void *param)
{
    int i;
    for (i = 0; i < 10000 ; i++)
        sum++;
    pthread_exit(0);
}

int main(void)
{
    pthread_t tid1, tid2;
    pthread_create(&tid1, NULL, run, NULL);
    pthread_create(&tid2, NULL, run, NULL);
    pthread_join(tid1, NULL);
    pthread_join(tid2, NULL);
    printf("result: %d\n", sum);
    return (0);
}
