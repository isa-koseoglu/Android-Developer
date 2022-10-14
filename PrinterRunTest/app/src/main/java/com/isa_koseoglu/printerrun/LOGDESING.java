package com.isa_koseoglu.printerrun;

public class LOGDESING {
    private ILogger[] _logger;
    LOGDESING(){

    }
    LOGDESING(ILogger[] logger){
        _logger=logger;
    }
    public void LogYaz(String content){
        if(_logger.length>0){
            for (ILogger log:_logger) {
                log.LOG(content);
            }
        }
        else{
            _logger=new ILogger[]{new CsystemPrint()};
            _logger[0].LOG(content);
        }

    }
}
interface ILogger{
    public void LOG(String content);
}
class CsystemPrint implements ILogger{

    @Override
    public void LOG(String content) {
        System.out.println(content);
    }
}
