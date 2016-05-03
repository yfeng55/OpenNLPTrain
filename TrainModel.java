import java.io.*;
import java.util.Arrays;

import opennlp.tools.cmdline.parser.TrainingParameters;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class TrainModel {

    public static void main(String[] args) throws IOException {

        ///// FORMAT TRAINING DATASET /////
        // formatTrainingData();


        ///// TRAIN /////

        DoccatModel model = null;
        InputStream dataIn = null;
        try {
            dataIn = new FileInputStream(args[0]);
            ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");

            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            // maxent model
            model = DocumentCategorizerME.train("en", sampleStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // save the trained model
        BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream("en-sentiment-model.bin"));
        model.serialize(modelOut);


        ///// TEST TRAINED MODEL /////
        testClassifier(model, "today was a great day");


    }


    public static void testClassifier(DoccatModel model, String testphrase){
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

        double[] outcomes = myCategorizer.categorize(testphrase);
        String category = myCategorizer.getBestCategory(outcomes);

        System.out.println(Arrays.toString(outcomes));

        if (category.equalsIgnoreCase("1")) {
            System.out.println("This phrase is positive");
        } else {
            System.out.println("This phrase is negative");
        }
    }


    public static void formatTrainingData() throws IOException {
        String fileName = "./sent_analysis_dataset.txt";
        String line = null;

        File fout = new File("shortened_sent_dataset.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));


        // FileReader reads text files in the default encoding.
        FileReader fileReader = new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // read the first x lines of the training set
        for(int i=0; i<50000; i++) {
            line = bufferedReader.readLine();
            String[] line_arr = line.split(",");
            String text = line_arr[3];

            if(line_arr[1].equals("1") || line_arr[1].equals("0")){
                for(int j=4; j<line_arr.length; j++){ text += "," + line_arr[j]; }
                //System.out.println(line_arr[1] + " " + text);

                bw.write(line_arr[1] + " " + text);
                bw.newLine();
            }

        }


        bufferedReader.close();
        bw.close();
    }


}