(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-notification', {
            parent: 'entity',
            url: '/user-notification?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserNotifications'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-notification/user-notifications.html',
                    controller: 'UserNotificationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('user-notification-detail', {
            parent: 'user-notification',
            url: '/user-notification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserNotification'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-notification/user-notification-detail.html',
                    controller: 'UserNotificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UserNotification', function($stateParams, UserNotification) {
                    return UserNotification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-notification-detail.edit', {
            parent: 'user-notification-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-notification/user-notification-dialog.html',
                    controller: 'UserNotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserNotification', function(UserNotification) {
                            return UserNotification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-notification.new', {
            parent: 'user-notification',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-notification/user-notification-dialog.html',
                    controller: 'UserNotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                isVisable: null,
                                isRead: null,
                                isCompleted: null,
                                notificationType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-notification', null, { reload: 'user-notification' });
                }, function() {
                    $state.go('user-notification');
                });
            }]
        })
        .state('user-notification.edit', {
            parent: 'user-notification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-notification/user-notification-dialog.html',
                    controller: 'UserNotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserNotification', function(UserNotification) {
                            return UserNotification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-notification', null, { reload: 'user-notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-notification.delete', {
            parent: 'user-notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-notification/user-notification-delete-dialog.html',
                    controller: 'UserNotificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserNotification', function(UserNotification) {
                            return UserNotification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-notification', null, { reload: 'user-notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
