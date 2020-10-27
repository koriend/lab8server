package serverPath;

public class Gen {
    public static void main(String[] args) {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .usePunctuation(true)
                .build();
        int i = 0;
        while(i<=19) {
            String randPass = passwordGenerator.generate(19);
            System.out.println(randPass);
            i++;
        }
    }
}
