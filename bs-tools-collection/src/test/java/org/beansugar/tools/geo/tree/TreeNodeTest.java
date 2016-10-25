package org.beansugar.tools.geo.tree;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TreeNode Tester.
 *
 * @author <Authors name>
 * @since <pre>07/01/2014</pre>
 * @version 1.0
 */
public class TreeNodeTest {

	TreeNode<Integer> node0;
	TreeNode<Integer> node1;
	TreeNode<Integer> node2;


	@Before
    public void setUp() throws Exception {
		node0 = new TreeNode<>(0);
		node1 = new TreeNode<>(1);
		node2 = new TreeNode<>(2);
		node0.put(1, node1).put(2, node2);
//		node1.setParent(node0);
//		node2.setParent(node0);
    }

	@After
    public void tearDown() {

    }

	@Test
	public void testSetParent() throws Exception {

		int item0 = node1.get(0).getValue();
		TreeNode<Integer> node11 = new TreeNode<>(11);
		node1.setParent(node11);
		int item1 = node1.get(0).getValue();
		Assert.assertNotSame(item0, item1);
		Assert.assertEquals(11, item1);
	}

	@Test
	public void testSetParent2SelectByNo() throws Exception {
		int item;

		System.out.println(node0);
		Assert.assertEquals(node0.get(0), null);
		item = node0.get(1).getValue();
		Assert.assertEquals(item, 1);
		item = node0.get(2).getValue();
		Assert.assertEquals(item, 2);

		node0.setParent(2);

		System.out.println(node0);
		item = node0.get(0).getValue();
		Assert.assertEquals(item, 2);
		Assert.assertEquals(node0.get(1), null);
		item = node0.get(2).getValue();
		Assert.assertEquals(item, 1);
	}

	@Test
	public void testSetParent2SelectByObj() throws Exception {
		System.out.println(node0);
		System.out.println(node1);
		System.out.println(node2);
		node0.setParent(node1);
		System.out.println(node0);
		System.out.println(node1);
		System.out.println(node2);
	}

	@Test
	public void testPut() throws Exception {
		TreeNode<Integer> tempnode = new TreeNode<>(11);

		Assert.assertNotSame(node1.get(0), tempnode);
		tempnode.put(0, node0);
		Assert.assertEquals(node0, tempnode.get(0));
	}

	@Test
	public void testGet() throws Exception {
		TreeNode<Integer> tempnode = new TreeNode<>(11);
		tempnode.put(0, node0);

		Assert.assertEquals(node0, tempnode.get(0));
	}

	@Test
	public void testChangeRoot() throws Exception {
		int item0 = node2.get(0).getValue();
		node2.makeRoot();
		int item1 = node2.get(2).getValue();
		Assert.assertEquals(item0, item1);
	}

	public static void main(String[] args){
		//============ 트리모형 생성
		TreeNode<Integer> node0 = new TreeNode<>(0);
		TreeNode<Integer> node1 = new TreeNode<>(1);
		TreeNode<Integer> node2 = new TreeNode<>(2);
		TreeNode<Integer> node3 = new TreeNode<>(3);
		TreeNode<Integer> node4 = new TreeNode<>(4);
		TreeNode<Integer> node5 = new TreeNode<>(5);
		TreeNode<Integer> node6 = new TreeNode<>(6);

		node0.put(1, node1).put(2, node2);
		node1.put(1, node3).put(2, node4);
		node2.put(1, node5).put(2, node6);
		//============ 트리모형 생성

		node0.showAll();

		System.out.println("\n\n");

		//순서 뒤집기 node4
		System.out.println("=============== node4 ===============");
		node4.makeRoot();
		System.out.println("node4 :"+node4+"\n");
		node4.showAll();

		System.out.println("\n\n");

		//순서 뒤집기 node6
		System.out.println("=============== node6 ===============");
		node6.makeRoot();
		System.out.println("node6 :"+node6+"\n");
		node6.showAll();
	}

}
