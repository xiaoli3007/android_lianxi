package com.sunli.sss;

class Woman extends Human
{
    /**
     * constructor
     */
    public Woman(int h)
    {
        super(h); // base class constructor
        
       
        System.out.println(super.getHeight());
        System.out.println("Hello, Pandora!");
    }

    /**
     * new method
     */
    public Human giveBirth()
    {
        System.out.println("Give birth");
        return (new Human(20));
    }

    /**
     * override Human.breath()
     */
    public void breath()
    {
        super.breath();
        System.out.println("su...");
    }
}


