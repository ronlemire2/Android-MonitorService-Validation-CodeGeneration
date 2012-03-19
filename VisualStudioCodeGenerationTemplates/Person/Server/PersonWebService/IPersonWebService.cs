using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.ServiceModel.Channels;
using System.ServiceModel.Activation;

namespace PersonWebService
{
   [ServiceContract]
   public interface IPersonWebService
   {
      [OperationContract]
      [WebGet(UriTemplate = "/PersonList", ResponseFormat = WebMessageFormat.Json)]
      PersonList GetPersonList();

      [OperationContract]
      [WebGet(UriTemplate = "/Person/{Id}", ResponseFormat = WebMessageFormat.Json)]
      PersonList GetPerson(string Id);

      [OperationContract]
      [WebInvoke(UriTemplate = "/Person/{Id}", Method = "PUT", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
      int SavePerson(string Id, Person addEditPerson);

      [OperationContract]
      [WebInvoke(UriTemplate = "/Person/{Id}", Method = "DELETE", ResponseFormat = WebMessageFormat.Json)]
      int DeletePerson(string Id);
   }
}
