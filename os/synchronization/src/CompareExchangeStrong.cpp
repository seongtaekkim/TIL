#include <iostream>
#include <string>
#include <thread>
#include <chrono>
#include <mutex>
#include <atomic>

using namespace std;

mutex mtx;  //mutual exclusion

int shared_memory(0); // race condition
//  atomic<long> shared_memory(0);
int main(void)
{
    auto count_func  =[]() {
        for (int i=0 ; i< 1000000 ; i++)
        {
            //this_thread::sleep_for(chrono::microseconds(1));
            mtx.lock();
                long value = shared_memory;
                long newValue = value + 1;
				shared_memory = newValue;
            mtx.unlock();


			// shared_memory++;

            // bool incSuccessful = false;
            // while(!incSuccessful) {
            //     long value = shared_memory;
            //     long newValue = value + 1;
				
			// 	/*
			// 		template <class _Tp>
			// 		_LIBCPP_INLINE_VISIBILITY
			// 		bool
			// 		atomic_compare_exchange_strong(volatile atomic<_Tp>* __o, _Tp* __e, _Tp __d) _NOEXCEPT
			// 		{
			// 			return __o->compare_exchange_strong(*__e, __d);
			// 		}
			// 	*/
            //     incSuccessful = shared_memory.compare_exchange_strong(value, newValue);
            // }

        }
    };


	int len = 100;
	thread t[len];

	for (int i=0 ; i< len ; i++) 
		t[i] = thread(count_func);

	for (int i=0 ; i< len ; i++) 
		t[i].join();

   
    cout << "After" << endl;
    cout << shared_memory << endl;


    return (0);
}