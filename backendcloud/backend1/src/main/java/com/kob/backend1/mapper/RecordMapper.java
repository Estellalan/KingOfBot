package com.kob.backend1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.kob.backend1.pojo.Record;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}