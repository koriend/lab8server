package collectionPath;

public class Executor implements Runnable {
    private Command command;

    public Executor(Command command){
        this.command = command;
    }

    @Override
    public void run() {
        this.command.execute();
    }
}
