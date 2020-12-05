import yaml

def config():
    with open("ganesanconfig.yaml", 'r') as ymlfile:
        cfg = yaml.load(ymlfile, Loader=yaml.FullLoader)

    for section in cfg:
        print(section)

    return cfg