package com.uid;

public class TestUniqueId {

	public static void main(String[] args) {
		Long uniqueId = new UniqueIdUtil().nextId();
		System.out.println(uniqueId);
	}
}
