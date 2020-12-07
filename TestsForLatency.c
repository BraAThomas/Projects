#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/time.h>

// Code for predicting latency time in a distributed computing
// MANET network vs. Cloud networks.

/* Data comes in the form of .txt numbered data to be
processed */
/* Data is either sent over the distributed MANET network 
where it is broken up and sent for processing to each 
available node, or sent to the Cloud for processing.*/

/* This experiment is to determine if the latency involved
with splitting up data and processing in the MANET is less
than, greater than, or equal to Cloud latency.*/

/* Assume all devices on the MANET have some processing 
capabilities and do not drop off the network. Also that 
data can only come in the form of raw .txt.*/

/* Variable studied: 
-time (data travel + processing)
    -Processing power of distributed network vs cloud
        through the time variable.  */

struct threadWork
{
    int devices, size;
    char fileC[4][50]; // Name of the different Celsius files to read
    char fileF[4][50]; // name of the different Fahrenhiet files to write
    FILE *tempfilesC[4]; // Pointers to the files themselvs
    FILE *tempfilesF[4];
    long int id[4]; // Ids for each thread
};

// Populates temperatureC.txt with random temperatures
// between 0 C and 50 C, resets the txt every time its run.
// This function does not count towards the time taken.
void popTempC(int numOfTemps)
{
    // Opens file with setting to rewrite file everytime.
    FILE *fp;
    fp = fopen("temperatureC.txt", "w");
    srand(time(0)); // set random seed
    // Populate file with random temperatures.
    // Each line is a new temperature.
    for (int i = 0; i < numOfTemps; i++)
    {
        fprintf(fp, "%d\n", rand() % 50);
    }
    fclose(fp);
}
// the Lock for thread use. 
pthread_mutex_t lock;

int thrid = 1;
// Scenario: Sensors send text data it collected over
//           Distributed MANET
// Take a list of temperatures in Celsius and change to Fahrenheit
void *dataManet(void *arg)
{
    // args brings in the temperature files, devices, size of the file (# of lines), ids for each thread. 
    struct threadWork *threads = (struct threadWork *)arg;
    // Buffer holds/converts temp data from string to integers
    char buffer[5][50];
    // Holds the data for each temp 
    int celsi[5], fahren[5];

    // Locks the id portion so each thread can identify which portion they should work on
    pthread_mutex_lock(&lock);
    threads->id[thrid - 1] = pthread_self();
    thrid++;
    pthread_mutex_unlock(&lock);

    // For all the devices
    for (int j = 0; j < threads->devices; j++)
    {
        // Find the j (portion of data 1st, 2nd etc) find your thread id
        if (pthread_self() == threads->id[j])
        {
            // Go through your portion to read, calculate and write. 
            for (int i = 0; i < threads->size / threads->devices; i++)
            {
                fscanf(threads->tempfilesC[j], "%s", buffer[j]);
                sscanf(buffer[j], "%d", &celsi[j]);
                fahren[j] = (celsi[j] * 1.8) + 32;
                fprintf(threads->tempfilesF[j], "%d\n", fahren[j]);
            }
            // Once done break out so that thread id reuse doesnt affect the results.
            break;
        }
    }
    return 0;
}

// Scenario: Sensors send text data it collected over
//           Cloud Network
// Take a list of temperatures in Celsius and change to Fahrenheit
int dataCloud(void *arg)
{
    // args brings in the temperature files, devices, size of the file (# of lines), ids for each thread. 
    struct threadWork *threads = (struct threadWork *)arg;
    // Holds the data for each temp
    int celsi, fahren;
    // Opens the files to read and write. Since this is cloud, there is only 1 for each.
    FILE *in = fopen("temperatureC.txt", "r");
    FILE *out = fopen("temperatureF.txt", "w");
    // Buffer holds/converts temp data from string to integers
    char buffer[4];

    // Go through the whole file and convert to them write. 
    for (int i = 0; i < threads->size; i++)
    {
        fscanf(in, "%s", buffer);
        sscanf(buffer, "%d", &celsi);
        fahren = (celsi * 1.8) + 32;
        fprintf(out, "%d\n", fahren);
    }
    return 0;
}

// Main to run the choice selection
int main(int argc, char *argv[])
{
    // Vars that dictate what portions of the code to run.
    int devices, scenario, datasize;
    // Established the start and end times for timing purposes
    struct timeval start_time, end_time;
    int time;
    void *arg;
    // The 5 options for size of the data
    long sizeTempData[5] = {1000, 2000, 5000, 7500, 10000};
    // Open a results file to write the final timing data to. 
    FILE *results = fopen("results.txt", "a+");

    //Populate the original temperature in Celsius file and get the size of the file.
    printf("Chose Data Size(0-4):\n 0: 1000\n 1: 2000\n 2: 5000\n 3: 7500\n 4: 10000\n");
    scanf("%d", &datasize);
    if(datasize < 0 || datasize > 4)
        perror("Incorrect value given for Data size.\n");
    popTempC(sizeTempData[datasize]);
    long size = sizeTempData[datasize];

    // Choose which scnario to test
    printf("Input scenario 1: For Cloud center test or 2: For Manet network test.\n");
    scanf("%d", &scenario);
    if(scenario <= 0 || scenario > 2)
        perror("Incorrect value given for Scenario.\n");
    switch (scenario)
    {
    case 1:
    {
        printf("How many devices? 1-4\n");
        scanf("%d", &devices);
        if(devices < 1 || devices > 4)
            perror("Incorrect value given for Number of Devices.\n");
        
        pthread_t threads[devices];
        // starts the threads id at 1
        int id = 1;
        // declare the size of the structs that hold the data for each of the threads
        struct threadWork *threadinfo = malloc(sizeof(long int) * 4 + sizeof(FILE) * 8 + 200 * sizeof(char) + 200 * sizeof(char) + sizeof(int) * 2);
        // Store information the threads will need to calculate further on.
        threadinfo->size = sizeTempData[datasize];

        char idstr[4];
        char buffer[4];
        FILE *fo, *fp, *fr;
        threadinfo->devices = devices;
        fp = fopen("temperatureC.txt", "r");
        // Create a new temperature C and F file for the amount of devices there are. Split up the original tempC file into the new files.
        for (int i = 1; i <= devices; i++)
        {
            // Build the names of the files that we will need. Based on how many devices have been declared. 
            // Concats the begining phrase with the id number and then .txt
            char filename[50] = "temperatureC";
            char filename2[50] = "temperatureF";
            sprintf(idstr, "%d", i);
            strcat(strcat(filename, idstr), ".txt");
            strcat(strcat(filename2, idstr), ".txt");
            // Opens the Celsius file so the data from the original celsius file can be properly copied
            fo = fopen(filename, "w");

            // Copy data from original tempC file to the new file that we are currently working on
            for (int j = 0; j < sizeTempData[datasize] / devices; j++)
            {
                fscanf(fp, "%s", buffer);
                fprintf(fo, "%s\n", buffer);
            }
            fclose(fo);

            // Put the new file names into the struct field to hold the names of all the files
            for (int k = 0; k < 50; k++)
            {
                threadinfo->fileC[i - 1][k] = filename[k];
                threadinfo->fileF[i - 1][k] = filename2[k];
            }

            // Open the files into the struct field that holds the files for processing.
            threadinfo->tempfilesC[i - 1] = fopen(filename, "r");
            threadinfo->tempfilesF[i - 1] = fopen(filename2, "w");
            threadinfo->id[i - 1] = i;
        }
        for (int i = 0; i < devices; i++)
            threadinfo->id[i] = 0;

        // Grab the start time and send the threads out to process the data.
        gettimeofday(&start_time, NULL);
        for (int i = 0; i < devices; i++)
        {
            if (pthread_create(&threads[i], NULL, dataManet, threadinfo) != 0)
            {
                fprintf(stderr, "pthread_create failed! \n");
                return EXIT_FAILURE;
            }
        }

        // Have all the threads join back together.
        for (int i = 0; i < devices; i++)
            pthread_join(threads[i], NULL);

        gettimeofday(&end_time, NULL);
        thrid = 1;
        // Time to have the 'devices' (threads) perform the (calculation + average Manet latency) + (size of data * calc'ed time * packet loss rate)
        // Used an AODVUU-RM data transfering model with 10Pckts/Sec that had a 90% success rate  (10% loss) and 90msec latency. 
        time = ((end_time.tv_usec - start_time.tv_usec)+ 90*1000000);
        time = time + (time * .10);
        fprintf(results, "Time taken, Manet, %d, microseconds, at, %ld, lines, with, %d devices.\n", time, size, devices);
    }
    break;
    // Implementation of the 'Cloud' based data processing. 
    case 2:
    {
        struct threadWork *threadinfo = malloc(sizeof(long int) * 4 + sizeof(FILE) * 8 + 400 + sizeof(int) * 2);
        // Since there is only 1 read and 1 write, simply open the files. 
        char filename[50] = "temperatureC.txt";
        char filename2[50] = "temperatureF.txt";
        // Store the names in the struct
        for (int k = 0; k < 50; k++)
        {
            threadinfo->fileC[0][k] = filename[k];
            threadinfo->fileF[0][k] = filename2[k];
        }
        // Store the open files in the struct. 
        threadinfo->tempfilesC[0] = fopen(filename, "r");
        threadinfo->tempfilesF[0] = fopen(filename2, "w");
        threadinfo->size = sizeTempData[datasize];
        // Time the function
        gettimeofday(&start_time, NULL);
        dataCloud(threadinfo);
        gettimeofday(&end_time, NULL);
        // Time is the exact time taken to do the data processing + latency to get to the location (average of www.CloudPing.info for the 4 US based servers)
        time = ((double)(end_time.tv_usec - start_time.tv_usec)) + ((128+144+90+93)/4) * 1000000;
        fprintf(results, "Time taken, Cloud, %d , microseconds, at, %ld , lines.\n", time, size);
        break;
    }
    default:
        printf("Invalid input for scenario choice.");
        perror("Invalid input");
    }

    return 0;
}