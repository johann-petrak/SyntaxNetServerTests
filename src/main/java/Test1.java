import cali.nlp.ParseyApi;
import cali.nlp.ParseyApi.ParseyRequest;
import cali.nlp.ParseyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import syntaxnet.SentenceOuterClass.Sentence;
import syntaxnet.SentenceOuterClass.Token;

class Test1  {
  
  
  
  public static void main(String[] args) {
    System.err.println("Running Test1"); 
    if(args.length != 2) {
      System.err.println("Need arguments: serverAddr serverPort");
      System.exit(1);
    }
    ManagedChannel channel = ManagedChannelBuilder.
            forAddress(args[0], Integer.parseInt(args[1])).
            usePlaintext(true).build();
    ParseyServiceGrpc.ParseyServiceBlockingStub stub = 
            ParseyServiceGrpc.newBlockingStub(channel);
    ParseyApi.ParseyRequest.Builder b =ParseyApi.ParseyRequest.newBuilder();
    b.addText("This is a sentence");
    b.addText("And another sentence");
    ParseyRequest req = b.build();
    System.err.println("Request built, trying to get response");
    ParseyApi.ParseyResponse resp;
    resp = stub.parse(req);
    List<Sentence> sentences = resp.getResultList();
    for(Sentence sentence : sentences) {
      System.err.println("Tokens for sentence: "+sentence.getText());
      List<Token> tokens = sentence.getTokenList();
      System.err.println("=> "+tokens);
    }
    try {
      channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
