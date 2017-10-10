package com.wugy.java.java8;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * devotion on 2017-04-12 8:59.
 */
public class StreamTest {

    @Test
    public void testStream1() {
        List<Article> acticles = new ArrayList<>();
        acticles.add(new Article("Java", "wugy", null));
        System.out.println(getArticleStream1(acticles).get().getTitle());
    }

    private static class Article {

        private final String title;
        private final String author;
        private final List<String> tags;

        private Article(String title, String author, List<String> tags) {
            this.title = title;
            this.author = author;
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public List<String> getTags() {
            return tags;
        }

    }

    // 1、传统遍历
    public Article getArticle1(List<Article> articles) {
        for (Article article : articles) {
            if (article.getTitle().equalsIgnoreCase("Java")) {
                return article;
            }
        }
        return null;
    }

    // 1、使用Stream：找到第一个匹配元素。
    public Optional<Article> getArticleStream1(List<Article> articles) {
        return articles.stream()
                .filter(article -> article.getTitle().equalsIgnoreCase("Java"))
                .findFirst();
    }

    // 2、使用Stream：找到所有匹配元素。
    public List<Article> getArticleStream2(List<Article> articles) {
        return articles.stream()
                .filter(article -> article.getTitle().equalsIgnoreCase("Java"))
                .collect(Collectors.toList());
    }

    // 根据作者来把所有的文章分组。
    public Map<String, List<Article>> groupByAuthor(List<Article> articles) {
        Map<String, List<Article>> result = new HashMap<>();
        for (Article article : articles) {
            if (result.containsKey(article.getAuthor())) {
                result.get(article.getAuthor()).add(article);
            } else {
                List<Article> articlesList = new ArrayList<>();
                articlesList.add(article);
                result.put(article.getAuthor(), articles);
            }
        }
        return result;
    }

    public Map<String, List<Article>> groupByAuthorStream(List<Article> articles) {
        return articles.stream()
                .collect(Collectors.groupingBy(Article::getAuthor));
    }

    // 查找集合中所有不同的标签。
    public Set<String> getDistinctTags(List<Article> articles) {
        Set<String> result = new HashSet<>();
        for (Article article : articles) {
            result.addAll(article.getTags());
        }
        return result;
    }

    public Set<String> getDistinctTagsStream(List<Article> articles) {
        return articles.stream()
                .flatMap(article -> article.getTags().stream())
                .collect(Collectors.toSet());
    }

    // 验证字符串是否包含集合中的某一字符串
    @Test
    public void test2() {
        List<String> keywords = Arrays.asList("brown", "fox", "dog", "pangram");
        String tweet = "The quick brown fox jumps over a lazy dog. #pangram http://www.rinkworks.com/words/pangrams.shtml";
        System.out.println(keywords.stream().allMatch(tweet::contains));;
        System.out.println(keywords.stream().reduce(false, (b, keyword) -> b || tweet.contains(keyword), (l, r) -> l || r));;
    }

}

