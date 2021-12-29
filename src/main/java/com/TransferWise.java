package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransferWise {

	public static void main(String[] args) {
		Book book1 = new Book();
		book1.uniqueId = 55321;
		Book book2 = new Book();
		book2.uniqueId = 55322;
		List<Book> book1Ref = new ArrayList<>();
		book1Ref.add(book2);
		List<Book> book2Ref = new ArrayList<>();
		book2Ref.add(book1);
		book1.references = book1Ref;
		book2.references = book2Ref;
		
		System.out.println("Total number of books to read -->" + computeTotalNumberOfBooksToRead(book1));
	}

	public static int computeTotalNumberOfBooksToRead(Book book) {
		Set<Integer> uniqueBooks = new HashSet<>();
		return computeTotalNumberOfBooksToReadRec(book, uniqueBooks).size();
	}

	public static Set<Integer> computeTotalNumberOfBooksToReadRec(Book book, Set<Integer> uniqueBooks) {
		if (book == null) {
                        /* Vijay : this could have worked without returning */
			return uniqueBooks;
		} else if (uniqueBooks.contains(book.uniqueId)) {
			return uniqueBooks;
		} else if (book.references == null) {
			uniqueBooks.add(book.uniqueId);
			return uniqueBooks;
		} else {
			uniqueBooks.add(book.uniqueId);
			for (Book currentBook : book.references) {
				computeTotalNumberOfBooksToReadRec(currentBook, uniqueBooks);
			}
		}
		return uniqueBooks;
	}

}

class Book {
	int uniqueId;
	String name;
	List<Book> references;
}
