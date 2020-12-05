import time

def start_time():
    # capture start time
    return time.time()

def elpased_time(start_time):

    end_time = time.time()
    # calculate elapsed time
    elapsed_time = end_time - start_time
    #print ("Code execution time in seconds is ",elapsed_time)
    elapsed_time_milliSeconds = elapsed_time*1000
    print(f'elapsed time in milliseconds is {elapsed_time_milliSeconds}')