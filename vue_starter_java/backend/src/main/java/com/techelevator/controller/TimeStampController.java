package com.techelevator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.authentication.AuthProvider;
import com.techelevator.authentication.RequestAuthProvider;
import com.techelevator.exception.PostNotFoundException;
import com.techelevator.exception.UserNotFoundException;
import com.techelevator.model.TimeStamp;
import com.techelevator.model.TimeStampDao;
import com.techelevator.model.User;


@RestController
@CrossOrigin
@RequestMapping("/stamp")
public class TimeStampController {

	private TimeStampDao timeStampDao;
	
	@Autowired
	private AuthProvider auth;
	
	public TimeStampController(TimeStampDao timeStampDao, AuthProvider auth) {
		this.timeStampDao = timeStampDao;
		this.auth = auth;
	}
	
	@GetMapping("/allstamps")
	public List<TimeStamp> getFeed() {
		List<TimeStamp> timeStamps = timeStampDao.getAllTimeStamps();
		return timeStamps;
	}
	
//	@GetMapping("/single_post/{post_id}")
//	public Post getPost(@PathVariable int post_id) throws PostNotFoundException {
//		Post post = postDao.getPostByPostId(post_id);
//		List<Comment> comments = commentDao.getCommentsByPostId(post_id);
//
//		if (post != null && comments != null) {
//			post.setComments(comments);
//			
//			
//		} else {
//			throw new PostNotFoundException(post_id, "Post Not Found!");
//		}
//		post.setNumberOfLikes(postDao.numberOfLikesOnPost(post_id));
//		post.setLiked(postDao.isLikedByUser(post_id, auth.getCurrentUser().getUsername()));
//		post.setFavorited(postDao.isFavoritedFromUser(post_id, auth.getCurrentUser().getUsername()));
//		return post;
//	}
	
	@GetMapping("/user/{username}")
	public List<TimeStamp> getUserPosts(@PathVariable String username) throws UserNotFoundException {
		List<TimeStamp> timeStamps = timeStampDao.getTimeStampsByUsername(username);
		if (timeStamps != null) {
			return timeStamps;
		} else {
			throw new UserNotFoundException(username, "User Not Found!");
		}
	}
	
	@RequestMapping(path = "/addstamp", method = RequestMethod.POST)
    public void addTimeStamp(@RequestBody TimeStamp newTimeStamp) {
		newTimeStamp.setUsername(auth.getCurrentUser().getUsername());
		timeStampDao.saveTimeStamp(newTimeStamp);
	}
	
}
