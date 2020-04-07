package com.carexpert.common;

import com.carexpert.entity.Item;
import lombok.Data;

import java.util.List;

@Data
public class NodeVO {
    Item self;
    List<Item> children;
}
