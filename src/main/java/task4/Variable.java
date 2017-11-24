package task4;

public class Variable
{
    String name;
    Double value;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public Variable(String name, Double value)
    {

        this.name = name;
        this.value = value;
    }
}
