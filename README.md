### OpenNLP Classifier Training

Formats a training set and trains a maximum entropy model for the OpenNLP classifier to use; outputs a file called en-sentiment-model.bin.

#### Compile and Run:
##### 1. run javac and specify external jars
```sh
javac -cp "./jars/*" *.java
```
##### 2. run program
```sh
java -cp ".:./jars/*" TrainModel [path to training data]
```
##### Example:
```sh
java -cp ".:./jars/*" TrainModel ./shortened_sent_dataset.txt
```
