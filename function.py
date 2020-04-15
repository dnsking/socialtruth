import json
import boto3
import base64
import random
import secrets
import string
import datetime

bucketName = 'socialtruthbucket'
SocialTruth ='SocialTruthTable'

def lambda_handler(event, context):
    # TODO implement
    if event['action'] == 'PutUrl':
        return putFile(event['Key'])
    elif event['action'] == 'SearchTruth':
        return searchForTruth(event['Key'])
    elif event['action'] == 'AddTruth':
        return addTruth(event['key'],event['url'],event['isTrue'],event['summary'],event['title'])
    elif event['action'] == 'SummarizeText':
        return json.dumps(summarizeText(event['key']))
        
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }


def putFile(key):
    s3 = boto3.client('s3', config=boto3.session.Config(signature_version='s3v4'))
    return s3.generate_presigned_url('put_object', Params={'Bucket':bucketName, 'Key':key}, ExpiresIn=21600, HttpMethod='PUT')
    
def getFile(key):
    s3 = boto3.client('s3', config=boto3.session.Config(signature_version='s3v4'))
    return s3.generate_presigned_url('get_object', Params={'Bucket':bucketName, 'Key':key}, ExpiresIn=21600)


def detect_text(key):

    client=boto3.client('rekognition')

    response=client.detect_text(Image={'S3Object':{'Bucket':bucketName,'Name':key}})
                        
    textDetections=response['TextDetections']
    #print(textDetections)
    lines = []
    for text in textDetections:
            if text['Type'] == "LINE":
                print(text['DetectedText'])
                lines.append(text['DetectedText'])
            
    return lines
def detect_textBytes(key):

    client=boto3.client('rekognition')

    response=client.detect_text(Image={'Bytes':base64.b64decode(key)})
    
    open('/tmp/filename.png', 'wb').write(base64.b64decode(key))
    
    
    s3 = boto3.resource('s3')
    s3.meta.client.upload_file('/tmp/filename.png', 'socialtruthbucket', 'filename.png')
                        
    textDetections=response['TextDetections']
    #print(textDetections)
    lines = []
    for text in textDetections:
            if text['Type'] == "LINE":
                print(text['DetectedText'])
                lines.append(text['DetectedText'])
            
    return lines
    
def searchForTruth(key):
    dynamodb = boto3.client('dynamodb')
    lines = detect_textBytes(key)
    resultCount = 0
    result = None
    for line in lines:
        try:
            response = dynamodb.get_item(Key={'line': {'S': line}},  TableName=SocialTruth)['Item']
            if response is not None and response['line'] is not None:
                resultCount = resultCount+1
                result = {"line":response['line']['S'],"url":response['url']['S'],"isTrue":response['isTrue']['S'],"title":response['title']['S'],"summary":response['summary']['S']}
        except:
            print("nothing found")
    return result 
            
    
def addTruth(key,url,isTrue,summary,title):
    lines = detect_text(key)
    addToDb(lines,url,isTrue,summary,title)
    return "added"
    
def addToDb(lines,url,isTrue,summary,title):
    alphabet = string.ascii_letters + string.digits
    fileName = ''.join(secrets.choice(alphabet) for i in range(20))
    fileName = fileName+".txt"
    
    s3client = boto3.client('s3')
    
    text = ""
    
    textlines = summary.splitlines()
    for textLine in textlines:
        newText = text+' '+textLine
        if len(newText)<5000:
            text = newText
    s3client.put_object(Body=text, Bucket='socialtruthbucket', Key=fileName)
    
    summary = summarizeText(fileName)
    
    dynamodb = boto3.client('dynamodb')
    for line in lines:
        dynamodb.put_item(TableName=SocialTruth,Item={'line': {'S': str(line)}, 'url': {'S': str(url)}, 'isTrue': {'S': str(isTrue)}, 'summary': {'S': str(summary)}, 'title': {'S': str(title)}})

def summarizeText(key):
    # TODO implement
    client = boto3.client('sagemaker-runtime')
     
    s3 = boto3.resource('s3')

    obj = s3.Object("socialtruthbucket", key)
    #obj.get()['Body'].read().decode('utf-8') 
    #s3://socialtruthbucket/self_driving_test.txt
    response = client.invoke_endpoint(EndpointName='Summarizer',
    ContentType="text/plain",Accept="text/plain", Body=obj.get()['Body'].read().decode('utf-8'))
    #response_body = response['Body'].read().decode("utf-8")
    print(response)
    body = response['Body'].read()
    #print(len(body.decode("utf-8")))
    print(body.decode("utf-8"))   
    return body.decode("utf-8")
