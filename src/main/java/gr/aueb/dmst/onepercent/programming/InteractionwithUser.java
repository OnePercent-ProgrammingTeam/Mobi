package gr.aueb.dmst.onepercent.programming;
import java.util.Scanner;
public class InteractionwithUser{
public void printMenu(){
    boolean flug = true;
    while (true){
        System.out.println("1 start");
        System.out.println("2 stop ");
        System.out.println("3 get information for running container ");
        System.out.println("4 get information for non running container");
        System.out.println("5 exit");
        Scanner input = new Scanner(System.in);
        int answer = input.nextInt();
        switch (answer) {
        case 1:
            System.out.println("start");
            //ContainerManagerHttp c1 = new ContainerManagerHttp();
            //c1.startContainer();
            break;
        case 2:
        System.out.println("stop");
            //ContainerManagerHttp c2 = new ContainerManagerHttp();
            //c2.stopContainer();
            break;
        case 3:
        System.out.println("get info");
            //ContainerManagerHttp c2 = new ContainerManagerHttp();
            //c2.stopContainer();
            break;
        case 4:
        System.out.println("get info non running");
            //ContainerManagerHttp c2 = new ContainerManagerHttp();
            //c2.stopContainer();
            break;
        case 5:
            flug = false;
            break;
        default:
        System.out.println("non valid");
        }
    }
}
}
