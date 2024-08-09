package Application.service;

import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;

@Service(name = "one")
public class CustomerService {
    public String name;

    public CustomerService(){
    }

    @Autowired
    public String getName(String name1, int age){
        System.out.println(name);
        return name1;
    }
}
