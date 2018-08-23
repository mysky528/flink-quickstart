package cn.jxau.yuan.scala.yuan.java.state.function;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * without implements checkpoint
 *
 * FlatMapFunction 为无状态函数
 *
 * RichFlatMapFunction 为有状态函数，因为可以获取getRuntimeContext().getstate
 */
public class CountWithKeyedState extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>> {

    private static final Logger LOG = LoggerFactory.getLogger("CountWithKeyedState");

    /**
     * The ValueState handle. The first field is the count, the second field a running sum.
     */
    private transient ValueState<Tuple2<Long, Long>> sum;

    @Override
    public void flatMap(Tuple2<Long, Long> input, Collector<Tuple2<Long, Long>> out) throws Exception {

        // access the state value
        Tuple2<Long, Long> currentSum = sum.value();

        // update the count
        currentSum.f0 += 1;

        // add the second field of the input value
        currentSum.f1 += input.f1;

        // update the state
        sum.update(currentSum);

        // 查看每个线程收到的input tuple
        LOG.warn(Thread.currentThread().getName() + "==>" + input.toString());

        // if the count reaches 3, emit the average and clear the state
        if (currentSum.f0 >= 3) {
            LOG.warn(new Tuple2<>(currentSum.f0, currentSum.f1 / currentSum.f0).toString());
            out.collect(new Tuple2<>(input.f0, currentSum.f1 / currentSum.f0));
            sum.clear();
        }
    }


    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<Tuple2<Long, Long>> descriptor =
                new ValueStateDescriptor<>("average", // the state name
                        TypeInformation.of(new TypeHint<Tuple2<Long, Long>>(){}),//type information
                        Tuple2.of(0L, 0L)); // default value of the state, if nothing was set
        // 显然这样获取状态更简单
        sum = getRuntimeContext().getState(descriptor);
    }
}