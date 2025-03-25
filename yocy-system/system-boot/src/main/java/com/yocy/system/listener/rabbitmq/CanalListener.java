package com.yocy.system.listener.rabbitmq;

import com.yocy.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Canal + RabbitMQ 监听数据库变化
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CanalListener {

    private final SysMenuService menuService;

//    @RabbitListener(queues = "canal.queue")
    public void handleDataChange() {}
}
