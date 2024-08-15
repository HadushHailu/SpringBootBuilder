package Application.service;

import Application.integration.ILogger;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Scheduled;
import org.autumframework.annotation.Service;

import java.time.LocalTime;

@Service
public class SchedulerService {
    @Autowired
    private ILogger logger;

    @Scheduled(cron = "5 0")
//    @Scheduled(fixedRate = 5000)
    public void scheduledLogAllProduct(){
        System.out.println("SchedulerService: "+ LocalTime.now() +" ");
        logger.log("All products");
    }
}
