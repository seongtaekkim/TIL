#include <iostream>
#include <string>
#include <thread>
#include <chrono>
#include <mutex>
#include <atomic>

using namespace std;

//mutex mtx;  //mutual exclusion

//int shared_memory(0); // race condition
atomic<int> shared_memory(0);
int main(void)
{
    auto count_func  =[]() {
        for (int i=0 ; i<1000000 ; i++)
        {
            //this_thread::sleep_for(chrono::microseconds(1));
            //mtx.lock();
            //shared_memory++;
            //mtx.unlock();

            bool incSuccessful = false;
            while(!incSuccessful) {
                int value = shared_memory;
                int newValue = value + 1;

                incSuccessful = shared_memory.compare_exchange_strong(value, newValue);
                //if (!incSuccessful)
                //    cout << value << endl;
            }

        }
    };

    thread t1 = thread(count_func);
    thread t2 = thread(count_func);
    thread t3 = thread(count_func);
    thread t4 = thread(count_func);
    thread t5 = thread(count_func);
    thread t6 = thread(count_func);
    thread t7 = thread(count_func);
    thread t8 = thread(count_func);
    thread t9 = thread(count_func);
    thread t10 = thread(count_func);


    t1.join();
    t2.join();
    t3.join();
    t4.join();
    t5.join();
    t6.join();
    t7.join();
    t8.join();
    t9.join();
    t10.join();

    cout << "After" << endl;
    cout << shared_memory << endl;


    return (0);
}