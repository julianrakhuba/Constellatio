package application;

//import rakhuba.application.Colors;

public class ColorsGenerator {

	public void generateColors() {
		Colors colors = new Colors();
		System.out.println("BLUE");
		System.out.println(colors.getLight(Colors.BLUE));
		System.out.println(colors.getMedium(Colors.BLUE));
		System.out.println(colors.getDark(Colors.BLUE));
		
		System.out.println("VIOLET");
		System.out.println(colors.getLight(Colors.VIOLET));
		System.out.println(colors.getMedium(Colors.VIOLET));
		System.out.println(colors.getDark(Colors.VIOLET));
		
		System.out.println("GREEN");
		System.out.println(colors.getLight(Colors.GREEN));
		System.out.println(colors.getMedium(Colors.GREEN));
		System.out.println(colors.getDark(Colors.GREEN));
	
		System.out.println("PINK");
		System.out.println(colors.getLight(Colors.PINK));
		System.out.println(colors.getMedium(Colors.PINK));
		System.out.println(colors.getDark(Colors.PINK));
		
		System.out.println("LAVANDER");
		System.out.println(colors.getLight(Colors.LAVANDER));
		System.out.println(colors.getMedium(Colors.LAVANDER));
		System.out.println(colors.getDark(Colors.LAVANDER));
		
		System.out.println("YELLOW");
		System.out.println(colors.getLight(Colors.YELLOW));
		System.out.println(colors.getMedium(Colors.YELLOW));
		System.out.println(colors.getDark(Colors.YELLOW));
				
		System.out.println("TORQUE");
		System.out.println(colors.getLight(Colors.BLUE2));
		System.out.println(colors.getMedium(Colors.BLUE2));
		System.out.println(colors.getDark(Colors.BLUE2));
	}
}
