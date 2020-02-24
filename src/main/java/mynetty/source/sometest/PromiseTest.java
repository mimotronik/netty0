package mynetty.source.sometest;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

/**
 * @author winterfell
 */
public class PromiseTest {

    public static void main(String[] args) {

        // 构造线程池
        EventExecutor executor = new DefaultEventExecutor();

        // 创建 DefaultPromise 实例
        Promise<Integer> promise = new DefaultPromise(executor);

        // promise 添加两个 listener

        promise.addListener(new GenericFutureListener<Future<Integer>>() {
            @Override
            public void operationComplete(Future<Integer> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("任务结束，结果" + future.get());
                } else {
                    System.out.println("任务失败，异常" + future.cause());
                }
            }
        });

        promise.addListener(new GenericFutureListener<Future<Integer>>() {
            @Override
            public void operationComplete(Future<Integer> future) throws Exception {
                System.out.println("任务结束 balabala....");
            }
        });

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    promise.setSuccess(123456);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            promise.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
