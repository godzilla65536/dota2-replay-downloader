docker stop dota2-replay-downloader
docker rm dota2-replay-downloader
docker run -d --name dota2-replay-downloader -v "C:\Program Files (x86)\Steam\steamapps\common\dota 2 beta\game\dota\replays:/replays" --restart always dota2-replay-downloader
pause