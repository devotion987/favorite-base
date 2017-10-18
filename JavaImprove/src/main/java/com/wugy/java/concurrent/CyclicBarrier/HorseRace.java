package com.wugy.java.concurrent.CyclicBarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CyclicBarrier使用场景：创建一组任务，它们并行的执行工作，然后在进行下一步骤之前等待，直至所有任务完成。
 * <p>
 * wugy on 2017/10/18 15:29
 */
public class HorseRace {

    private static final int FINISH_LINE = 75;
    private List<Horse> horses = new ArrayList<>();
    private ExecutorService service = Executors.newCachedThreadPool();

    public HorseRace(int nHorses, final int pause) {
        CyclicBarrier barrier = new CyclicBarrier(nHorses, () -> {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < FINISH_LINE; i++)
                s.append("=");
            System.out.println(s);

            horses.forEach(horse -> System.out.println(horse.tracks()));

            for (Horse horse : horses) {
                if (horse.getStrides() > FINISH_LINE) {
                    System.out.println(horse + " win!");
                    service.shutdownNow();
                    return;
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < nHorses; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            service.execute(horse);
        }
    }

    public static void main(String[] args) {
        new HorseRace(7, 200);
    }

}
