from pytube import Playlist
from multiprocessing import Pool

def download_video(video_url, download_directory):
    try:
        from pytube import YouTube
        video = YouTube(video_url)
        stream = video.streams.filter(res="720p").first()
        if stream:
            print(f"Downloading: {video.title}")
            stream.download(output_path=download_directory)
            print(f"Downloaded: {video.title}")
        else:
            print(f"720p stream not available for: {video.title}")
    except Exception as e:
        print(f"Error downloading {video_url}: {str(e)}")

if __name__ == "__main__":
    # URL of the YouTube playlist
    playlist_url = 'https://www.youtube.com/playlist?list=PLpLBSl8eY8jT9NQplAyFtR2eDBnRP2jZl'

    # Directory to save the downloaded videos
    download_directory = 'D:/Html Css Js/advanced'

    # Create a Playlist object
    playlist = Playlist(playlist_url)

    # Specify the URL of the starting video
    #if video url in youtube is: https://www.youtube.com/watch?v=PbH47NBABh0&list=PLpLBSl8eY8jT9NQplAyFtR2eDBnRP2jZl&index=2
    #set url like below
    starting_video_url = 'https://www.youtube.com/watch?v=PbH47NBABh0'  # Replace with your URL

    # Convert the DeferredGeneratorList to a regular list and find the index
    video_urls_list = list(playlist.video_urls)
    starting_index = video_urls_list.index(starting_video_url)

    # Choose the number of parallel downloads (at least 3)
    num_parallel_downloads = 3

    # Create a Pool of worker processes
    with Pool(num_parallel_downloads) as pool:
        pool.starmap(download_video, [(video_url, download_directory) for video_url in video_urls_list[starting_index:starting_index + num_parallel_downloads]])

    print("Download completed!")
